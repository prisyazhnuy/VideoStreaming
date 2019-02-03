package com.prisyazhnuy.streaming.network.api.converters

import com.prisyazhnuy.streaming.enums.GenderType
import com.prisyazhnuy.streaming.models.User
import com.prisyazhnuy.streaming.models.UserModel
import com.prisyazhnuy.streaming.models.converters.BaseConverter
import com.prisyazhnuy.streaming.network.api.beans.UserBean

interface UserBeanConverter

class UserBeanConverterImpl : BaseConverter<UserBean, User>(), UserBeanConverter {

    override fun processConvertInToOut(inObject: UserBean) = inObject.run {
        UserModel(id, email, firstName, lastName, avatarUrl, phone, GenderType.byValue(gender), dateOfBirth)
    }

    override fun processConvertOutToIn(outObject: User) = outObject.run {
        UserBean(id, email, firstName, lastName, avatarUrl, phone, gender?.invoke(), dateOfBirth)
    }
}
