package ru.fefu.wsr_connect_mobile.tasks.board_list


class BoardsRepository {
    private val boardsList = listOf(
        BoardItem(
            0,
            "Board name 1",
            "Dec 21",
            "15",
            "Creator",
            null,
            true
        ),
        BoardItem(
            0,
            "Board name 1",
            "Dec 21",
            "15",
            "Creator",
            null,
            true
        ),
        BoardItem(
            0,
            "Board name 1",
            "Dec 21",
            "15",
            "Creator",
            null,
            true
        ),
        BoardItem(
            0,
            "Board name 1",
            "Dec 21",
            "15",
            "Creator",
            null,
            false
        ),
        BoardItem(
            0,
            "Board name 1",
            "Dec 21",
            "15",
            "Creator",
            null,
            false
        ),
        BoardItem(
            0,
            "Board name 1",
            "Dec 21",
            "15",
            "Creator",
            null,
            false
        ),
    )

    fun getBoardsList(): List<BoardItem> = boardsList
}