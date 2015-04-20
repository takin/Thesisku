package SemanticQA.listeners;

import java.util.List;

/**
 * Listener untuk memoitor proses tokenisasi dan PPOS Tagging
 * @author syamsul
 */
public interface TokenizerListener {
    
    void onTokenizeSuccess(List<String> taggedToken);
    void onTokenizeFail(String reason);
    
}
