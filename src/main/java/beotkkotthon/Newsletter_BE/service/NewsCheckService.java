package beotkkotthon.Newsletter_BE.service;

public interface NewsCheckService {
    //공지를 확인하면 READ로 변경
    void readNews(Long memberId, Long newsId);
}
