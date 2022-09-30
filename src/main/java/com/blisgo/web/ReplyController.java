package com.blisgo.web;

import com.blisgo.service.ReplyService;
import com.blisgo.web.dto.AccountDTO;
import com.blisgo.web.dto.BoardDTO;
import com.blisgo.web.dto.ReplyDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RestController
@RequestMapping("reply")
public class ReplyController {
    private final ReplyService replyService;
    private final ModelAndView mv = new ModelAndView();
    String url;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    /**
     * 댓글 작성
     *
     * @param accountDTO  사용자
     * @param boardDTO 게시글
     * @param replyDTO 댓글
     * @return mv
     */
    @PostMapping("{bdNo}")
    public ModelAndView replyPOST(AccountDTO accountDTO, BoardDTO boardDTO, @Valid ReplyDTO replyDTO) {
        replyService.addReply(replyDTO, boardDTO, accountDTO);
        url = RouteUrlHelper.combine(page.board, boardDTO.getBdNo());
        mv.setView(new RedirectView(url, false));
        return mv;
    }

    /**
     * 댓글 삭제
     *
     * @param boardDTO 게시글
     * @param replyDTO 댓글
     * @return mv
     */
    @DeleteMapping("{bdNo}/{replyNo}")
    public ModelAndView replyRemove(BoardDTO boardDTO, ReplyDTO replyDTO) {
        replyService.removeReply(replyDTO, boardDTO);
        url = RouteUrlHelper.combine(page.board, boardDTO.getBdNo());
        mv.setView(new RedirectView(url, false));
        return mv;
    }
}
