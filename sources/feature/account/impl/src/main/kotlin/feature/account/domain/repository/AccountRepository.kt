package feature.account.domain.repository

import feature.account.domain.entity.Account
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface AccountRepository {

    fun getAccountList(): Flow<Result<List<Account>>>

    suspend fun addBalance(account: Account): Result<Unit>

    suspend fun removeBalance(account: Account, commission: BigDecimal): Result<Unit>
}
