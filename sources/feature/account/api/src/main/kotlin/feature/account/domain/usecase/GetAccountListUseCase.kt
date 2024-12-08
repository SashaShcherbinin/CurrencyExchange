package feature.account.domain.usecase

import feature.account.domain.entity.Account
import kotlinx.coroutines.flow.Flow

interface GetAccountListUseCase : () -> Flow<Result<List<Account>>>