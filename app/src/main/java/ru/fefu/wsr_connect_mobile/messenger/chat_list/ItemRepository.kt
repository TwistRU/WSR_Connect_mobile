package ru.fefu.wsr_connect_mobile.messenger.chat_list

class ItemRepository {
    private val newChatList = listOf(
        ItemNewChat(0, "FirstName", "LastName", null),
        ItemNewChat(1, "FirstName", "LastName", null),
        ItemNewChat(2, "FirstName", "LastName", null),
        ItemNewChat(3, "FirstName", "LastName", null),
        ItemNewChat(4, "FirstName", "LastName", null),
        ItemNewChat(5, "FirstName", "LastName", null),
        ItemNewChat(6, "FirstName", "LastName", null),
        ItemNewChat(7, "FirstName", "LastName", null),
        ItemNewChat(8, "FirstName", "LastName", null),
        ItemNewChat(9, "FirstName", "LastName", null),
        ItemNewChat(10, "FirstName", "LastName", null),
        ItemNewChat(11, "FirstName", "LastName", null),
        ItemNewChat(12, "FirstName", "LastName", null),
        ItemNewChat(13, "FirstName", "LastName", null),
        ItemNewChat(14, "FirstName", "LastName", null),
        ItemNewChat(15, "FirstName", "LastName", null),
        ItemNewChat(16, "FirstName", "LastName", null),
        ItemNewChat(17, "FirstName", "LastName", null),
        ItemNewChat(18, "FirstName", "LastName", null),
        ItemNewChat(19, "FirstName", "LastName", null),
        ItemNewChat(20, "FirstName", "LastName", null),
        ItemNewChat(21, "FirstName", "LastName", null),
        ItemNewChat(22, "FirstName", "LastName", null),
    )

    private val chatList = listOf(
        ItemChat(0, "ChatName", "LastMessage", isMute = true, isPinned = true, null),
        ItemChat(1, "ChatName", "LastMessage", isMute = true, isPinned = true, null),
        ItemChat(2, "ChatName", "LastMessage", isMute = false, isPinned = true, null),
        ItemChat(3, "ChatName", "LastMessage", isMute = false, isPinned = true, null),
        ItemChat(4, "ChatName", "LastMessage", isMute = false, isPinned = true, null),
        ItemChat(13, "ChatName", "LastMessage", isMute = false, isPinned = false, null),
        ItemChat(14, "ChatName", "LastMessage", isMute = true, isPinned = false, null),
        ItemChat(15, "ChatName", "LastMessage", isMute = false, isPinned = false, null),
        ItemChat(16, "ChatName", "LastMessage", isMute = false, isPinned = false, null),
        ItemChat(17, "ChatName", "LastMessage", isMute = false, isPinned = false, null),
        ItemChat(18, "ChatName", "LastMessage", isMute = false, isPinned = false, null),
        ItemChat(19, "ChatName", "LastMessage", isMute = true, isPinned = false, null),
        ItemChat(20, "ChatName", "LastMessage", isMute = true, isPinned = false, null),
        ItemChat(21, "ChatName", "LastMessage", isMute = true, isPinned = false, null),
    )

    fun getNewChatList(): List<MessengerItem> = newChatList
    fun getChatList(): List<MessengerItem> = chatList
}