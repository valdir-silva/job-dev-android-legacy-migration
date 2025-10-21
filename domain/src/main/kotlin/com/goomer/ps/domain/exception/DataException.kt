package com.goomer.ps.domain.exception

sealed class DataException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

class LocalDataLoadException(
    message: String,
    cause: Throwable? = null,
) : DataException(message, cause)

class RepositoryException(
    message: String,
    cause: Throwable? = null,
) : DataException(message, cause)

