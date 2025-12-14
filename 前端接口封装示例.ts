/**
 * 校园智能助手 API 前端封装
 * 
 * 基于以下接口：
 * - GET  /api/v1/chat - 简单对话（无状态）
 * - POST /api/v2/chat - 高级对话（带记忆）
 * - GET  /api/v2/history/{userId} - 查询历史
 * 
 * 使用说明：
 * 1. 修改 baseUrl 为实际的后端地址
 * 2. 根据项目需求选择合适的接口（v1 或 v2）
 * 3. 对于多轮对话，务必使用相同的 userId
 */

// ==================== 类型定义 ====================

/**
 * 请求DTO
 */
export interface RequestDTO {
  userId: string;    // 用户唯一标识
  message: string;   // 用户消息内容
}

/**
 * 助手响应
 */
export interface AssistantResponse {
  userId?: string;              // 用户ID
  threadId?: string;            // 线程ID
  answer: string;               // AI回答
  type: 'weather' | 'course' | 'library' | 'general';  // 回答类型
  suggestion: string;           // 建议
  needsFurtherHelp: boolean;    // 是否需要进一步帮助
}

/**
 * 历史记录响应
 */
export interface HistoryResponse {
  userId: string;
  threadId: string;
  history: string;
}

/**
 * 错误响应
 */
export interface ErrorResponse {
  error: string;
}

// ==================== 配置 ====================

const BASE_URL = 'http://localhost:8082';

// ==================== API 封装 ====================

/**
 * 简单对话接口（无状态）
 * 适合：快速测试、简单查询、不需要上下文的情况
 * 
 * @param question 用户问题
 * @returns AI 回答（纯文本）
 */
export async function simpleChat(question: string = '图书馆在哪里'): Promise<string> {
  const url = `${BASE_URL}/api/v1/chat?question=${encodeURIComponent(question)}`;
  
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.text();
  } catch (error) {
    console.error('Simple chat request failed:', error);
    throw error;
  }
}

/**
 * 高级对话接口（带记忆）
 * 适合：多轮对话、个性化服务、需要上下文的情况
 * 
 * @param userId 用户ID（相同userId会共享对话上下文）
 * @param message 用户消息
 * @returns 助手响应
 */
export async function advancedChat(
  userId: string,
  message: string
): Promise<AssistantResponse> {
  const url = `${BASE_URL}/api/v2/chat`;
  
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userId,
        message,
      } as RequestDTO),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json() as AssistantResponse;
  } catch (error) {
    console.error('Advanced chat request failed:', error);
    throw error;
  }
}

/**
 * 查询对话历史
 * 
 * @param userId 用户ID
 * @returns 历史记录或错误信息
 */
export async function getHistory(
  userId: string
): Promise<HistoryResponse | ErrorResponse> {
  const url = `${BASE_URL}/api/v2/history/${userId}`;
  
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json() as HistoryResponse | ErrorResponse;
  } catch (error) {
    console.error('Get history request failed:', error);
    throw error;
  }
}

// ==================== 会话管理类 ====================

/**
 * 对话会话管理类
 * 封装了多轮对话的逻辑，自动管理 userId 和 threadId
 */
export class ChatSession {
  private userId: string;
  private threadId?: string;

  /**
   * 构造函数
   * @param userId 用户ID（如果未提供，会自动生成）
   */
  constructor(userId?: string) {
    this.userId = userId || this.generateUserId();
    // 尝试从本地存储恢复 threadId
    const savedThreadId = localStorage.getItem(`threadId_${this.userId}`);
    if (savedThreadId) {
      this.threadId = savedThreadId;
    }
  }

  /**
   * 生成用户ID（用于游客用户）
   */
  private generateUserId(): string {
    const stored = localStorage.getItem('guestUserId');
    if (stored) {
      return stored;
    }
    const newUserId = `guest_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    localStorage.setItem('guestUserId', newUserId);
    return newUserId;
  }

  /**
   * 发送消息
   * @param message 用户消息
   * @returns 助手响应
   */
  async sendMessage(message: string): Promise<AssistantResponse> {
    const response = await advancedChat(this.userId, message);
    
    // 保存 threadId
    if (response.threadId) {
      this.threadId = response.threadId;
      localStorage.setItem(`threadId_${this.userId}`, response.threadId);
    }
    
    return response;
  }

  /**
   * 获取对话历史
   */
  async getHistory(): Promise<HistoryResponse | ErrorResponse> {
    return await getHistory(this.userId);
  }

  /**
   * 获取当前用户ID
   */
  getUserId(): string {
    return this.userId;
  }

  /**
   * 获取当前线程ID
   */
  getThreadId(): string | undefined {
    return this.threadId;
  }

  /**
   * 重置会话（清除本地存储的 threadId）
   */
  reset(): void {
    localStorage.removeItem(`threadId_${this.userId}`);
    this.threadId = undefined;
  }
}

// ==================== 使用示例 ====================

/**
 * 示例1：简单对话
 */
export async function example1_SimpleChat() {
  const answer = await simpleChat('今天天气怎么样');
  console.log('AI回答:', answer);
}

/**
 * 示例2：高级对话（单次）
 */
export async function example2_AdvancedChat() {
  const response = await advancedChat('user123', '帮我查一下今天的天气');
  console.log('AI回答:', response.answer);
  console.log('回答类型:', response.type);
  console.log('线程ID:', response.threadId);
}

/**
 * 示例3：多轮对话
 */
export async function example3_MultiTurnChat() {
  const session = new ChatSession('user123');

  // 第一轮
  const response1 = await session.sendMessage('我是学号20220001的学生');
  console.log('第一轮回答:', response1.answer);

  // 第二轮（AI会记住学号）
  const response2 = await session.sendMessage('帮我查一下我的课程表');
  console.log('第二轮回答:', response2.answer);
}

/**
 * 示例4：使用会话类（自动管理userId）
 */
export async function example4_ChatSession() {
  // 自动生成或使用已有的userId
  const session = new ChatSession();

  const response = await session.sendMessage('图书馆在哪里');
  console.log('用户ID:', session.getUserId());
  console.log('线程ID:', session.getThreadId());
  console.log('AI回答:', response.answer);
}

/**
 * 示例5：查询历史
 */
export async function example5_GetHistory() {
  const history = await getHistory('user123');
  
  if ('error' in history) {
    console.error('错误:', history.error);
  } else {
    console.log('用户ID:', history.userId);
    console.log('线程ID:', history.threadId);
    console.log('历史记录:', history.history);
  }
}

/**
 * 示例6：React Hook 使用示例
 */
export function useChatSession(userId?: string) {
  const [session] = React.useState(() => new ChatSession(userId));
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState<Error | null>(null);

  const sendMessage = async (message: string): Promise<AssistantResponse | null> => {
    setLoading(true);
    setError(null);
    try {
      const response = await session.sendMessage(message);
      return response;
    } catch (err) {
      setError(err as Error);
      return null;
    } finally {
      setLoading(false);
    }
  };

  return {
    sendMessage,
    loading,
    error,
    userId: session.getUserId(),
    threadId: session.getThreadId(),
  };
}

// ==================== 错误处理工具 ====================

/**
 * 带错误处理的聊天函数
 */
export async function safeChat(
  userId: string,
  message: string
): Promise<AssistantResponse | null> {
  try {
    return await advancedChat(userId, message);
  } catch (error) {
    console.error('Chat request failed:', error);
    return null;
  }
}

/**
 * 带重试的聊天函数
 */
export async function chatWithRetry(
  userId: string,
  message: string,
  maxRetries: number = 3
): Promise<AssistantResponse> {
  let lastError: Error | null = null;

  for (let i = 0; i < maxRetries; i++) {
    try {
      return await advancedChat(userId, message);
    } catch (error) {
      lastError = error as Error;
      console.warn(`Chat request failed (attempt ${i + 1}/${maxRetries}):`, error);
      
      // 等待后重试（指数退避）
      if (i < maxRetries - 1) {
        await new Promise(resolve => setTimeout(resolve, Math.pow(2, i) * 1000));
      }
    }
  }

  throw lastError || new Error('Chat request failed after retries');
}

// ==================== 导出 ====================

export default {
  simpleChat,
  advancedChat,
  getHistory,
  ChatSession,
  safeChat,
  chatWithRetry,
};





