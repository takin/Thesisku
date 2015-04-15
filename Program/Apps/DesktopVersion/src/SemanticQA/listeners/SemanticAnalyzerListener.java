package SemanticQA.listeners;

import java.util.List;

/**
 * interface untuk memonitor proses pembentukan parse tree
 * @author syamsul
 */
public interface SemanticAnalyzerListener {
    
    public void onAnalyzeSuccess(List parseTree);
    public void onAnalyzeFail(String reason);
    
}
