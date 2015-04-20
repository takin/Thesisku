package SemanticQA.listeners;

import java.util.List;

/**
 * interface untuk memonitor proses pembentukan parse tree
 * @author syamsul
 */
public interface SemanticAnalyzerListener {
    
    void onAnalyzeSuccess(List parseTree);
    void onAnalyzeFail(String reason);
    
}
