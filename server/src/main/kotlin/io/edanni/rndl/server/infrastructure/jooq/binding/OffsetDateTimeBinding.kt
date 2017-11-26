package io.edanni.rndl.server.infrastructure.jooq.binding

import io.edanni.rndl.server.infrastructure.jooq.converter.OffsetDateTimeConverter
import org.jooq.*
import org.jooq.impl.DSL
import org.threeten.bp.OffsetDateTime

class OffsetDateTimeBinding : Binding<java.time.OffsetDateTime, OffsetDateTime> {
    private val converter = OffsetDateTimeConverter()

    override fun sql(ctx: BindingSQLContext<OffsetDateTime>?) {
        ctx?.render()?.visit(DSL.sql("?"))
    }

    override fun get(ctx: BindingGetResultSetContext<OffsetDateTime>?) {
        ctx?.convert(converter())?.value(ctx.resultSet().getObject(ctx.index()) as java.time.OffsetDateTime?)
    }

    override fun get(ctx: BindingGetStatementContext<OffsetDateTime>?) {
        ctx?.convert(converter())?.value(ctx.statement().getObject(ctx.index()) as java.time.OffsetDateTime?)
    }

    override fun set(ctx: BindingSetStatementContext<OffsetDateTime>?) {
        ctx?.statement()?.setObject(ctx.index(), ctx.convert(converter()).value())
    }

    override fun converter(): Converter<java.time.OffsetDateTime, OffsetDateTime> = converter

    override fun get(ctx: BindingGetSQLInputContext<OffsetDateTime>?) = throw UnsupportedOperationException()

    override fun set(ctx: BindingSetSQLOutputContext<OffsetDateTime>?) = throw UnsupportedOperationException()

    override fun register(ctx: BindingRegisterContext<OffsetDateTime>?) = throw UnsupportedOperationException()
}