package feature.account.data.repository

import feature.account.domain.entity.Account
import feature.account.domain.exception.NoEnoughMoneyException
import feature.account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal

class AccountRepositoryImpl : AccountRepository {

    private var account: MutableStateFlow<List<Account>> = MutableStateFlow(
        listOf(
            Account(
                currency = "EUR",
                amount = 1000.toBigDecimal(),
            )
        )
    )

    override fun getAccountList(): Flow<Result<List<Account>>> {
        return account.map { Result.success(it) }
    }

    override suspend fun addBalance(account: Account): Result<Unit> {
        val existingBalance = this.account.value.find { it.currency == account.currency }
        val newBalanceList = if (existingBalance != null) {
            this.account.value.map {
                if (it.currency == account.currency) {
                    it.copy(amount = it.amount + account.amount)
                } else {
                    it
                }
            }
        } else {
            this.account.value + account
        }
        this.account.value = newBalanceList
        return Result.success(Unit)
    }

    override suspend fun removeBalance(account: Account, commission: BigDecimal): Result<Unit> {
        val existingBalance = this.account.value.find { it.currency == account.currency }
        if (existingBalance == null) {
            return Result.failure(NoEnoughMoneyException())
        }

        val newBalanceList = this.account.value.map {
            if (it.currency == account.currency) {
                val newAmount = it.amount - account.amount - commission
                if (newAmount > BigDecimal.ZERO) {
                    it.copy(amount = newAmount)
                } else {
                    return Result.failure(NoEnoughMoneyException())
                }
            } else {
                it
            }
        }
        this.account.value = newBalanceList
        return Result.success(Unit)
    }
}
