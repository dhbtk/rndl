package io.edanni.rndl.server.infrastructure.jooq.binding

import io.edanni.rndl.server.infrastructure.jooq.converter.LocalDateTimeConverter
import org.jooq.*
import org.jooq.impl.DSL
import org.threeten.bp.LocalDateTime
import java.sql.Timestamp

class LocalDateTimeBinding : Binding<Timestamp, LocalDateTime> {
    private val converter = LocalDateTimeConverter()

    override fun sql(ctx: BindingSQLContext<LocalDateTime>?) {
        ctx?.render()?.visit(DSL.sql("?"))
    }

    override fun get(ctx: BindingGetResultSetContext<LocalDateTime>?) {
        ctx?.convert(converter())?.value(ctx.resultSet().getObject(ctx.index()) as Timestamp?)
    }

    override fun get(ctx: BindingGetStatementContext<LocalDateTime>?) {
        ctx?.convert(converter())?.value(ctx.statement().getObject(ctx.index()) as Timestamp?)
    }

    override fun set(ctx: BindingSetStatementContext<LocalDateTime>?) {
        ctx?.statement()?.setObject(ctx.index(), ctx.convert(converter()).value())
    }

    override fun converter(): Converter<Timestamp, LocalDateTime> = converter

    override fun get(ctx: BindingGetSQLInputContext<LocalDateTime>?) = throw UnsupportedOperationException()

    override fun set(ctx: BindingSetSQLOutputContext<LocalDateTime>?) = throw UnsupportedOperationException()

    override fun register(ctx: BindingRegisterContext<LocalDateTime>?) = throw UnsupportedOperationException()
}