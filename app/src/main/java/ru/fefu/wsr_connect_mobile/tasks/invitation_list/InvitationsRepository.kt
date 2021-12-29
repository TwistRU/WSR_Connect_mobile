package ru.fefu.wsr_connect_mobile.tasks.invitation_list


class InvitationsRepository {
    private val invitationsList = listOf(
        InvitationItem(
            0,
            "Some company name 1",
            "Hey, we don't have enough hands in the company, but you have just two of them",
            null
        ),
        InvitationItem(
            1,
            "Some company name 2",
            "Hey, we don't have enough...",
            null
        ),
        InvitationItem(
            2,
            "Some company name 3",
            "Hey, we don't have enough hands in the company, but you have just two of them" +
                    " Hey, we don't have enough hands in the company, but you have just two of them",
            null
        ),
        InvitationItem(
            3,
            "Some company name 4",
            "Hey, we don't have enough hands in the company, but you have just two of them" +
                    " Hey, we don't have enough hands in the company, but you have just two of them",
            null
        ),
        InvitationItem(
            4,
            "Some company name 5",
            "Hey, we don't have enough hands in the company, but you have just two of them" +
                    " Hey, we don't have enough hands in the company, but you have just two of them",
            null
        ),
        InvitationItem(
            5,
            "Some company name 6",
            "Hey, we don't have enough hands in the company, but you have just two of them" +
                    " Hey, we don't have enough hands in the company, but you have just two of them",
            null
        ),
        InvitationItem(
            6,
            "Some company name 7",
            "Hey, we don't have enough hands in the company, but you have just two of them" +
                    " Hey, we don't have enough hands in the company, but you have just two of them",
            null
        ),
        InvitationItem(
            7,
            "Some company name 8",
            "Hey, we don't have enough hands in the company, but you have just two of them" +
                    " Hey, we don't have enough hands in the company, but you have just two of them",
            null
        ),
    )

    fun getInvitationsList(): List<InvitationItem> = invitationsList
}