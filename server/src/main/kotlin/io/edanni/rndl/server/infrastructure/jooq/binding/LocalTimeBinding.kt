package io.edanni.rndl.server.infrastructure.jooq.binding

import io.edanni.rndl.server.infrastructure.jooq.converter.LocalTimeConverter
import org.jooq.*
import org.jooq.impl.DSL
import org.threeten.bp.LocalTime
import java.sql.Time

class LocalTimeBinding : Binding<Time, LocalTime> {
    private val timeConverter = LocalTimeConverter()

    override fun sql(ctx: BindingSQLContext<LocalTime>?) {
        ctx?.render()?.visit(DSL.sql("?"))
    }

    override fun get(ctx: BindingGetResultSetContext<LocalTime>?) {
        ctx?.convert(converter())?.value(ctx.resultSet().getObject(ctx.index()) as Time?)
    }

    override fun get(ctx: BindingGetStatementContext<LocalTime>?) {
        ctx?.convert(converter())?.value(ctx.statement().getObject(ctx.index()) as Time?)
    }

    override fun set(ctx: BindingSetStatementContext<LocalTime>?) {
        ctx?.statement()?.setObject(ctx.index(), ctx.convert(converter()).value())
    }

    override fun get(ctx: BindingGetSQLInputContext<LocalTime>?) = throw UnsupportedOperationException()

    override fun set(ctx: BindingSetSQLOutputContext<LocalTime>?) = throw UnsupportedOperationException()

    override fun register(ctx: BindingRegisterContext<LocalTime>?) = throw UnsupportedOperationException()

    override fun converter(): Converter<Time, LocalTime> = timeConverter
}