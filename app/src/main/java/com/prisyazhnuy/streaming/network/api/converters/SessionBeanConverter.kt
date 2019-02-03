package com.prisyazhnuy.streaming.network.api.converters

import com.prisyazhnuy.streaming.models.Session
import com.prisyazhnuy.streaming.models.SessionModel
import com.prisyazhnuy.streaming.models.converters.BaseConverter
import com.prisyazhnuy.streaming.network.api.beans.SessionBean

interface SessionBeanConverter

class SessionBeanConverterImpl : BaseConverter<SessionBean, Session>(), SessionBeanConverter {

    override fun processConvertInToOut(inObject: SessionBean) = inObject.run {
        SessionModel(accessToken, refreshToken, expiresAt)
    }

    override fun processConvertOutToIn(outObject: Session) = outObject.run {
        SessionBean(accessToken, refreshToken, expireDate)
    }
}
