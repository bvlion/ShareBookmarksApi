import io.mockk.every
import io.mockk.mockk
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager

object MockDatabase {
  class TestTransactionManager : TransactionManager {
    override var defaultIsolationLevel = 0
    override var defaultRepetitionAttempts = 0
    private val mockedDatabase: Database = mockk(relaxed = true)

    override fun currentOrNull(): Transaction {
      return transaction()
    }

    private fun transaction(): Transaction {
      return mockk(relaxed = true) {
        every { db } returns mockedDatabase
      }
    }

    override fun newTransaction(isolation: Int, outerTransaction: Transaction?): Transaction {
      return transaction()
    }

    fun apply() {
      TransactionManager.registerManager(mockedDatabase, this)
      Database.connect({ mockk(relaxed = true) }, { this })
    }

    fun reset() {
      TransactionManager.resetCurrent(null)
      TransactionManager.closeAndUnregister(mockedDatabase)
    }
  }

  private val manager = TestTransactionManager()
  fun mock() = manager.apply()
  fun unMock() = manager.reset()
}