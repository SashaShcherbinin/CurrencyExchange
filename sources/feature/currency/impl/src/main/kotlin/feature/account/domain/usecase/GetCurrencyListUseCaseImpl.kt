package feature.account.domain.usecase

import feature.account.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow

class GetCurrencyListUseCaseImpl(
    private val currencyRepository: CurrencyRepository,
) : GetCurrencyListUseCase {

    override fun invoke(): Flow<Result<List<String>>> =
        currencyRepository.getCurrencyList()
}
