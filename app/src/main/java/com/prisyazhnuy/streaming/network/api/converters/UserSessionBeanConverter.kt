package com.prisyazhnuy.streaming.network.api.converters

import com.prisyazhnuy.streaming.enums.GenderType
import com.prisyazhnuy.streaming.models.User
import com.prisyazhnuy.streaming.models.UserModel
import com.prisyazhnuy.streaming.models.converters.BaseConverter
import com.prisyazhnuy.streaming.network.api.beans.UserSessionBean


interface UserSessionBeanConverter

class UserSessionBeanConverterImpl : BaseConverter<UserSessionBean, User>(), UserSessionBeanConverter {

    override fun processConvertInToOut(inObject: UserSessionBean) = inObject.run {
        UserModel(id, email, firstName, lastName, avatarUrl, phone, GenderType.byValue(gender), dateOfBirth)
    }

    override fun processConvertOutToIn(outObject: User) = outObject.run {
        UserSessionBean(id, email, firstName, lastName, avatarUrl, phone, gender?.invoke(), dateOfBirth)
    }
}
