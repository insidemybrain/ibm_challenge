package com.ibm.challenge.statement.domain.model

import com.ibm.challenge.domain.model.DomainModel

data class StatementResponseDomain(val statementList: List<StatementDomain>? = null,
                                   val error: ErrorDomain? = null): DomainModel()