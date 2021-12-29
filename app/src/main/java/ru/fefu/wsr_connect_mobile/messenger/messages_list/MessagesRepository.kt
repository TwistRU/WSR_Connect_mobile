package ru.fefu.wsr_connect_mobile.messenger.messages_list

class MessagesRepository {
    private val messagesList = listOf(
        ItemSimpleMessage(
            0,
            read = true,
            "The tech billionaire's SpaceX satellites are catching heat",
            "20:33"
        ),
        ItemSimpleMessageOther(
            0,
            "The tech billionaire's SpaceX satellites are catching heat",
            "20:33"
        ),
        ItemSimpleMessage(
            0,
            read = true,
            "in the country after Beijing complained that two satellites launched by the American aerospace manufacturer endangered Chinese astronauts.",
            "20:33"
        ),
        ItemSimpleMessageOther(
            0,
            "in the country after Beijing complained that two satellites launched by the American aerospace manufacturer endangered Chinese astronauts.",
            "20:33"
        ),
        ItemReplyMessage(
            2,
            read = true,
            "ire's SpaceX satellites are catch",
            "You",
            "in the country after Beijing complained that two satellites launched by the American aerospace manufa",
            "12:22"
        ),
        ItemReplyMessageOther(
            2,
            "ire's SpaceX satellites are catch",
            "You",
            "in the country after Beijing complained that two satellites launched by the American aerospace manufa",
            "12:22"
        ),
        ItemSimpleMessage(
            0,
            read = true,
            "catching heat",
            "20:33"
        ),
        ItemSimpleMessageOther(
            0,
            "catching heat",
            "20:33"
        ),
        ItemImageMessageOther(
            1,
            null,
            "noMinePicture.png",
            "0.5 mb",
            "20:21"
        ),
        ItemImageMessage(
            1,
            read = true,
            null,
            "mine.png",
            "0.7 mb",
            "20:21"
        ),
        ItemImageMessageOther(
            1,
            null,
            "mine.png",
            "0.7 mb",
            "20:21"
        ),
        ItemImageMessage(
            1,
            read = false,
            null,
            "noMinePicture.png",
            "0.5 mb",
            "20:21"
        ),
        ItemReplyMessage(
            2,
            read = false,
            "ire's SpaceX SpaceX",
            "no You",
            "ospace manufa",
            "12:22"
        ),
    )

    fun getMessagesList(): List<MessageItem> = messagesList
}