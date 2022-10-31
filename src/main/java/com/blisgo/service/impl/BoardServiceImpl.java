package com.blisgo.service.impl;

import com.blisgo.domain.entity.Board;
import com.blisgo.domain.mapper.AccountMapper;
import com.blisgo.domain.mapper.BoardMapper;
import com.blisgo.domain.repository.BoardRepository;
import com.blisgo.service.BoardService;
import com.blisgo.util.HtmlContentParse;
import com.blisgo.web.dto.AccountDTO;
import com.blisgo.web.dto.BoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private static int index = 0;
    private static final int limit = 12;

    @Override
    public boolean addBoard(BoardDTO boardDTO, AccountDTO accountDTO) {
        var board = BoardMapper.INSTANCE.toEntity(boardDTO);
        var account = AccountMapper.INSTANCE.toEntity(accountDTO);

        String boardThumbnail = HtmlContentParse.parseThumbnail(board.getBdContent());
        board = Board.createBoardWithThumbnail(account, board, boardThumbnail);
        return boardRepository.insertBoard(board);
    }

    @Override
    public List<BoardDTO> findBoards() {
        String bdContentImgRemoved;
        index = 0;
        List<BoardDTO> board = new ArrayList<>();
        @SuppressWarnings("lint")
        var rs = boardRepository.selectBoardList(index, limit);
        // List<BoardDTO> boardDTOArray = BoardMapper.INSTANCE.toDTOList(rs);
        @SuppressWarnings("lint")
        List<BoardDTO> boardDTOArray = rs;

        for (BoardDTO b : boardDTOArray) {
            bdContentImgRemoved = HtmlContentParse.parseContentPreview(b.getBdContent());
            b = BoardDTO.selectBoardFilterContentImage(b, bdContentImgRemoved);
            board.add(b);
        }

        return board;
    }

    @Override
    public List<BoardDTO> findBoardMore() {
        String bdContentImgRemoved;
        // 더보기
        index += limit;

        List<BoardDTO> board = new ArrayList<>();
        @SuppressWarnings("lint")
        var rs = boardRepository.selectBoardList(index, limit);
        // List<BoardDTO> boardDTOArray = BoardMapper.INSTANCE.toDTOList(rs);
        @SuppressWarnings("lint")
        List<BoardDTO> boardDTOArray = rs;

        for (BoardDTO b : boardDTOArray) {
            bdContentImgRemoved = HtmlContentParse.parseContentPreview(b.getBdContent());
            b = BoardDTO.selectBoardFilterContentImage(b, bdContentImgRemoved);
            board.add(b);
        }

        return board;
    }

    @Override
    public BoardDTO findBoard(BoardDTO boardDTO) {
        var board = BoardMapper.INSTANCE.toEntity(boardDTO);
        var rs = boardRepository.selectBoard(board);
        return BoardMapper.INSTANCE.toDTO(rs);
    }

    @Override
    public boolean removeBoard(BoardDTO boardDTO) {
        var board = BoardMapper.INSTANCE.toEntity(boardDTO);
        return boardRepository.deleteBoard(board);
    }

    @Override
    public boolean countBoardViews(BoardDTO boardDTO) {
        var board = BoardMapper.INSTANCE.toEntity(boardDTO);
        return boardRepository.updateBoardViews(board);
    }

    @Override
    public boolean modifyBoard(BoardDTO boardDTO) {
        var board = BoardMapper.INSTANCE.toEntity(boardDTO);
        return boardRepository.updateBoard(board);
    }

    @Override
    public boolean countBoardFavorite(BoardDTO boardDTO) {
        var board = BoardMapper.INSTANCE.toEntity(boardDTO);
        return boardRepository.updateBoardFavorite(board);
    }
}
