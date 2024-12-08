package feature.account.domain.usecase

import feature.account.domain.entity.Transfer

interface TransferMoneyUseCase : suspend (Transfer) -> Result<Unit>