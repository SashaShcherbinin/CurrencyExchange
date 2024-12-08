package feature.account.domain.usecase

import feature.account.domain.entity.Account
import feature.account.domain.entity.Transfer
import feature.account.domain.repository.AccountRepository

class TransferMoneyUseCaseImpl(
    private val accountRepository: AccountRepository,
) : TransferMoneyUseCase {

    override suspend fun invoke(transfer: Transfer): Result<Unit> {
        val fromAccount = Account(
            currency = transfer.fromCurrency,
            amount = transfer.fromAmount,
        )
        val toAccount = Account(
            currency = transfer.toCurrency,
            amount = transfer.toAmount,
        )

        val removeResult = accountRepository.removeBalance(
            account = fromAccount,
            commission = transfer.commission,
        )

        return removeResult.map {
            accountRepository.addBalance(toAccount)
        }
    }
}

