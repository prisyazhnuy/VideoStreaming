package com.prisyazhnuy.streaming.ui_activity_test

val invalidMinLengthPasswords = arrayOf(
        "123",
        "фывфы",
        "asdf",
        "12123",
        "12ф3",
        "12aa3",
        "aaaa",
        "asdds")

val invalidMaxLengthPasswords = arrayOf(
        "123adfdkjsfdasfkdjas;flkadsfjddasfasdfasd",
        "123adfdkjsfdasfkdjas;flkadsfjddasfasdfasd",
        "123adfdkjsfdasfkdjas;aaaaaaasdfsdfasdf213123123123",
        "asdvxcvxcvxcvxcvxcvxvxcvx;aaaaaaasdfsdfasdf213123123123",
        "фывфы14108310298341028394018412312341234134")

//By default our NameValidator check only for name length, so here is the list of valid names
val validNames = arrayOf(
        "Vadim",
        "Вадим",
        "вадим",
        "Nikita",
        "Никита",
        "никита",
        "Sergei",
        "Сергей",
        "сергей",
        "Mikhail",
        "Михаил",
        "миша",
        "Anton",
        "Антон",
        "антон",
        "Andrey",
        "Андрей",
        "андрей",
        "Valentin",
        "Валентин",
        "валик",
        "Max",
        "Максим",
        "макс",
        "Aleksey",
        "Алексей",
        "лёша",
        "Nick",
        "Николай",
        "коля",
        "Oleh",
        "Олег",
        "олег",
        "Александр",
        "саша",
        "Alex")

val invalidNames = arrayOf(
        "фываав123123;flkadsfjddфasfasdfasd",
        "228фывфыв1488;flkadsfjddфывафываыasfasdfasd",
        "123adfdkjsfdasfkdjas;aaaaaaasdfфывафывафываsdfasdf213123123123",
        "asdvxcvxcvxcvxcvxcvxvxcvx;aaaaaaasdfsdfasdf213123123123",
        "фывфы14108310298341028394018412312341234134")