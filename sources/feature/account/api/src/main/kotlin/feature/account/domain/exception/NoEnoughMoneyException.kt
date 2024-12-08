package feature.account.domain.exception

class NoEnoughMoneyException : Exception() {
    override val message: String
        get() = "No enough money"

}