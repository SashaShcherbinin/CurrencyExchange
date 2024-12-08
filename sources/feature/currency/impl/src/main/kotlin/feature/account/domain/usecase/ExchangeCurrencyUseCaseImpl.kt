package feature.account.domain.usecase

import feature.account.domain.entity.ExchangeRequest
import feature.account.domain.entity.ExchangeResult
import feature.account.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow

class ExchangeCurrencyUseCaseImpl(private val exchangeRepository: CurrencyRepository) :
    ExchangeCurrencyUseCase {

    override fun invoke(exchangeRequest: ExchangeRequest): Flow<Result<ExchangeResult>> =
        exchangeRepository.exchange(exchangeRequest)
}
