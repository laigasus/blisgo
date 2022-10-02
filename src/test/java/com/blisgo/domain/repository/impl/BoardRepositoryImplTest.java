package com.blisgo.domain.repository.impl;

import com.blisgo.config.TestQueryDslConfig;
import com.blisgo.domain.entity.Account;
import com.blisgo.domain.entity.Board;
import com.blisgo.domain.repository.BoardRepository;
import com.blisgo.web.dto.BoardDTO;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@DataJpaTest
@Import(TestQueryDslConfig.class)
class BoardRepositoryImplTest {
    enum entityAssistOpt {
        PERSIST, AUTOINCREMEMT
    }

    @Autowired
    EntityManager entityManager;
    @Autowired
    BoardRepository boardRepository;
    Javers javers;
    Diff diff;
    Account sampleAccount;
    Board sampleBoard;

    private void entityAssistant(@NotNull entityAssistOpt opt, Object... entities) {
        switch (opt) {
            case PERSIST:
                for (var e : entities)
                    entityManager.persist(e);
                break;

            case AUTOINCREMEMT:
                for (var e : entities)
                    entityManager.createNativeQuery(String.format("ALTER TABLE %s AUTO_INCREMENT = 1", e.getClass().getSimpleName())).executeUpdate();
                break;

            default:
                break;
        }
    }

    private void initData() {
        sampleAccount = Account.builder().nickname("nickname").email("email").pass("pass").memPoint(0).profileImage("profileImage").build();
        sampleBoard = Board.builder().account(sampleAccount).bdTitle("bdTitle").bdCategory("bdCategory").bdContent("bdContent").bdViews(0).bdFavorite(0).bdReplyCount(0).bdThumbnail("bdThumbnail").build();
    }


    @BeforeEach
    void monitorEntity() {
        initData();
        entityAssistant(entityAssistOpt.PERSIST, sampleAccount, sampleBoard);
        javers = JaversBuilder.javers().withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE).build();
    }

    @AfterEach
    void manageEntity() {
        if (diff != null) {
            System.out.println("감사 결과>" + diff.changesSummary());
            diff.getChanges().forEach(change -> System.out.println("- " + change));
        } else {
            log.info("해당 테스트에서는 엔티티 감사 안했음");
        }
        entityManager.clear();
        entityAssistant(entityAssistOpt.AUTOINCREMEMT, sampleAccount, sampleBoard);
    }

    @Nested
    @DisplayName("board.html")
    class BoardPage {
        @Test
        @DisplayName("게시글 다건 조회되는가?")
        void testSelectBoardList() {
            List<BoardDTO> result = boardRepository.selectBoardList(0, 24);
            Assertions.assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("content.html")
    class ContentPage {
        @Test
        @DisplayName("게시글이 추가되었는가?")
        void testInsertBoard() {
            boolean result = boardRepository.insertBoard(sampleBoard);
            Assertions.assertTrue(result);
        }

        @Test
        @DisplayName("게시글 단건 조회되는가?")
        void testSelectBoard() {
            Board result = boardRepository.selectBoard(sampleBoard);
            Assertions.assertNotNull(result);
        }

        @Test
        @DisplayName("게시글이 삭제되었는가?")
        void testDeleteBoard() {
            boolean result = boardRepository.deleteBoard(sampleBoard);
            Assertions.assertTrue(result);
        }

        @Test
        @DisplayName("게시글 조회수가 올랐는가?")
        void testUpdateBoardViews() {
            boolean result = boardRepository.updateBoardViews(sampleBoard);
            Assertions.assertTrue(result);
        }

        @Test
        @DisplayName("게시글이 수정되었는가?")
        void testUpdateBoard() {
            boolean result = boardRepository.updateBoard(sampleBoard);
            Assertions.assertTrue(result);
        }

        @Test
        @DisplayName("게시글 좋아요가 올랐는가?")
        void testUpdateBoardFavorite() {
            boolean result = boardRepository.updateBoardFavorite(sampleBoard);
            Assertions.assertTrue(result);
        }
    }


}