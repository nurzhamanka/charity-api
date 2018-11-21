package kz.peep.api.entities.log

import kz.peep.api.entities.audit.DateAudit

abstract class LogEntry (
        val message: String
) : DateAudit() {
        abstract val flag: String
}