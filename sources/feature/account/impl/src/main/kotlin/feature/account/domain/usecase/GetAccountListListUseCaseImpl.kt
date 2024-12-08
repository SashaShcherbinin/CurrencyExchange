package feature.account.domain.usecase

import feature.account.domain.entity.Account
import feature.account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAccountListListUseCaseImpl(
    private val accountRepository: AccountRepository,
) : GetAccountListUseCase {

    override fun invoke(): Flow<Result<List<Account>>> {
        return accountRepository.getAccountList()
    }
}
