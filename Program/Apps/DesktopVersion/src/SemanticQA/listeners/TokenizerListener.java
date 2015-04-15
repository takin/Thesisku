package SemanticQA.listeners;

import java.util.List;

/**
 * Listener untuk memoitor proses tokenisasi dan PPOS Tagging
 * @author syamsul
 */
public interface TokenizerListener {
    
    public void onTokenizeSuccess(List<String> taggedToken);
    public void onTokenizeFail(String reason);
    
}
