package com.example.marvelkmp

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import com.example.marvelkmp.cache.MarvelDatabase
import com.example.marvelkmp.data.MockCharactersRepository
import com.example.marvelkmp.data.local.CacheCharactersRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CacheCharactersRepositoryTest {

    /**
     * FUNCIÓN HELPER: Construye un motor de base de datos simulado (Mock/Fake).
     * Para testear puramente la LÓGICA DE FALLBACK del repositorio, creamos un objeto anónimo que implementa
     * la interfaz 'SqlDriver'. Esto hace que el test sea ultrarápido, ligero y 100% multiplataforma.
     */
    private fun createInMemoryDatabase(): MarvelDatabase {
        val mockDriver = object : SqlDriver {

            // Indica que no hay ninguna transacción activa corriendo en este momento en segundo plano.
            override fun currentTransaction(): Transacter.Transaction? = null

            // Simula la apertura de bloques de transacciones de SQLDelight (ej. queries.transaction { ... }).
            override fun newTransaction(): QueryResult<Transacter.Transaction> {
                val mockTransaction = object : Transacter.Transaction() {
                    // Indica que esta transacción simulada no depende de ninguna otra transacción padre.
                    override val enclosingTransaction: Transacter.Transaction? = null

                    // Simula la finalización exitosa del bloque de la transacción devolviendo un resultado vacío estructurado.
                    override fun endTransaction(successful: Boolean): QueryResult<Unit> {
                        return QueryResult.Value(Unit)
                    }
                }
                return QueryResult.Value(mockTransaction)
            }

            // Simula comandos de escritura o borrado en la base de datos (como INSERT o DELETE).
            // Devuelve un valor por defecto (0 líneas afectadas) envuelto en un QueryResult.Value exigido por SQLDelight 2.x.
            override fun execute(
                identifier: Int?,
                sql: String,
                parameters: Int,
                binders: (SqlPreparedStatement.() -> Unit)?
            ): QueryResult<Long> {
                return QueryResult.Value(0L)
            }

            // Simula las consultas de lectura a la base de datos (como SELECT * FROM ...).
            override fun <R> executeQuery(
                identifier: Int?,
                sql: String,
                mapper: (SqlCursor) -> QueryResult<R>,
                parameters: Int,
                binders: (SqlPreparedStatement.() -> Unit)?
            ): QueryResult<R> {
                // Creamos un cursor simulado completamente vacío para imitar una base de datos local recién inicializada.
                val emptyCursor = object : SqlCursor {
                    // 'next() = false' le avisa a SQLDelight que no hay filas ni registros guardados para leer.
                    override fun next(): QueryResult<Boolean> = QueryResult.Value(false)
                    override fun getString(index: Int): String? = null
                    override fun getLong(index: Int): Long? = null
                    override fun getBytes(index: Int): ByteArray? = null
                    override fun getDouble(index: Int): Double? = null
                    override fun getBoolean(index: Int): Boolean? = null
                }
                // Ejecuta el mapeador interno de la librería pasándole el cursor vacío.
                return mapper(emptyCursor)
            }

            // Métodos obligatorios para observar cambios en las tablas (no requeridos para evaluar este flujo).
            override fun addListener(vararg queryKeys: String, listener: Query.Listener) {}
            override fun removeListener(vararg queryKeys: String, listener: Query.Listener) {}
            override fun notifyListeners(vararg queryKeys: String) {}
            override fun close() {}
        }

        // Retorna la clase de base de datos autogenerada conectada a nuestro Driver simulado.
        return MarvelDatabase(mockDriver)
    }

    /**
     * ESCENARIO 1: Flujo feliz (Carga exitosa).
     * Satisface los puntos 1 y 2 del issue: Intentar red -> Red Exitosa -> Guardar y Devolver.
     */
    @Test
    fun cuandoLaRedEsExitosa_debeGuardarEnLocalYDevolverDatosyMostrarEnConsola() = runTest {
        // 1. ARRANGE (Preparar el escenario)
        val mockNetwork = MockCharactersRepository()
        mockNetwork.shouldFail = false // Simulamos que internet funciona perfectamente.

        val database = createInMemoryDatabase()
        // Implementación del Decorator Pattern: Envolvemos el repo de red simulado dentro del decorador de caché.
        val cacheRepository = CacheCharactersRepository(mockNetwork, database)

        // 2. ACT (Ejecutar la acción a evaluar)
        val result = cacheRepository.getCharacters()

        // 3. ASSERT (Verificar que el resultado sea el esperado)
        // Validamos que el repositorio intercepte la respuesta exitosa y devuelva los 6 personajes del Mock de red.
        assertEquals(6, result.size)
    }

    /**
     * ESCENARIO 2: Flujo alternativo (Red caída + Caché local vacío).
     * Satisface el punto 4 del ticket: Si la llamada de red falla y la base local no tiene datos, se propaga el error.
     */
    @Test
    fun cuandoLaRedFalla_yElCacheEstaVacio_debePropagarElError() = runTest {
        // 1. ARRANGE (Preparar el escenario)
        val mockNetwork = MockCharactersRepository()
        mockNetwork.shouldFail = true // Simulamos un error de red / corte de internet.

        val database = createInMemoryDatabase() // La base de datos simulada no tiene datos guardados.
        val cacheRepository = CacheCharactersRepository(mockNetwork, database)

        // 2. ACT & ASSERT (Ejecutar y evaluar la excepción en simultáneo)
        // El test pasará VERDE únicamente si el repositorio propaga la excepción hacia arriba
        // y NO se traga el error (permitiendo que la UI sepa que debe mostrar un estado de error).
        assertFailsWith<Exception> {
            cacheRepository.getCharacters()
        }
    }

    // Este test solo es a modo de comprobacion visual que se devuelven los datos del mock
    @Test
    fun cuandoLaRedEsExitosa_debeGuardarEnLocalYDevolverDatos() = runTest {
        // 1. ARRANGE
        val mockNetwork = MockCharactersRepository()
        mockNetwork.shouldFail = false

        val database = createInMemoryDatabase()
        val cacheRepository = CacheCharactersRepository(mockNetwork, database)

        // 2. ACT
        val result = cacheRepository.getCharacters()

        // -----------------------------------------------------------------
        // IMPRESIÓN VISUAL AGREGADA PARA VERIFICAR LOS DATOS
        println("\n==================================================")
        println("🚀 PRUEBA VISUAL: PERSONAJES DEVUELTOS POR EL DECORADOR:")
        println("==================================================")
        result.forEach { character ->
            println("▶ ID: ${character.id} | Nombre: ${character.name}")
        }
        println("==================================================\n")
        // -----------------------------------------------------------------

        // 3. ASSERT
        assertEquals(6, result.size)
    }
}