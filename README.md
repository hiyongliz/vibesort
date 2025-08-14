# VibeSort

一个基于 OpenAI API 的智能排序 Java 库。

## 功能特性

- 🤖 使用 AI 进行智能排序
- 📝 支持自定义排序条件
- 🔧 简单易用的 API
- ⚡ 基于 OpenAI GPT 模型
- 🌍 支持多种 AI 服务提供商
- 🔐 环境变量配置，保护 API 密钥安全
- ✅ 完整的单元测试覆盖

## 安装

### Maven

```xml
<dependency>
    <groupId>com.vibesort</groupId>
    <artifactId>vibesort</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.vibesort:vibesort:1.0.0'
```

## 使用方法

### 1. 引入 vibesort 库

通过 Maven 或 Gradle 添加依赖。

### 2. 配置环境变量（推荐）

设置以下环境变量：

```bash
# 必需：OpenAI API 密钥
export OPENAI_API_KEY="your-openai-api-key"

# 可选：指定模型（默认：gpt-3.5-turbo）
export OPENAI_MODEL="gpt-4"

# 可选：自定义 API 端点（默认：OpenAI 官方 API）
export OPENAI_BASE_URL="https://your-custom-endpoint.com/v1/chat/completions"
```

### 3. 创建一个 Sort 类的实例

```java
import com.vibesort.Sort;

// 方式一：使用环境变量（推荐）
Sort sort = new Sort();

// 方式二：手动指定参数
Sort sort = new Sort("your-openai-api-key");
Sort sort = new Sort("your-openai-api-key", "gpt-4");
Sort sort = new Sort("your-openai-api-key", "gpt-4", "https://custom-api.com/v1/chat/completions");
```

### 4. 调用 Sort 类的 sort 方法

```java
import java.util.Arrays;
import java.util.List;

List<String> items = Arrays.asList("苹果", "香蕉", "樱桃");

try {
    // 基本排序（字母顺序）
    List<String> sorted = sort.sort(items);
    System.out.println("排序结果: " + sorted);
    
    // 自定义排序条件
    List<String> customSorted = sort.sort(items, "按颜色从浅到深");
    System.out.println("自定义排序: " + customSorted);
} catch (IOException e) {
    System.err.println("排序失败: " + e.getMessage());
}
```

## 快速开始

1. **设置环境变量**
   ```bash
   export OPENAI_API_KEY="your-api-key"
   # 可选：使用其他模型或端点
   export OPENAI_MODEL="gpt-4"
   export OPENAI_BASE_URL="https://your-endpoint.com/v1/chat/completions"
   ```

2. **运行示例代码**
   ```java
   import com.vibesort.Sort;
   import java.util.Arrays;
   
   public class Example {
       public static void main(String[] args) {
           try {
               Sort sort = new Sort();
               var items = Arrays.asList("dog", "cat", "elephant", "ant");
               var sorted = sort.sort(items, "按动物大小从小到大");
               System.out.println(sorted); // [ant, cat, dog, elephant]
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
   ```

## API 文档

### Sort 类

#### 构造函数

- `Sort()` - 使用环境变量创建实例（推荐）
- `Sort(String apiKey)` - 使用默认模型创建实例
- `Sort(String apiKey, String model)` - 指定模型创建实例
- `Sort(String apiKey, String model, String baseUrl)` - 完全自定义配置

#### 环境变量

- `OPENAI_API_KEY` - OpenAI API 密钥（必需）
- `OPENAI_MODEL` - 模型名称（可选，默认：gpt-3.5-turbo）
- `OPENAI_BASE_URL` - API 基础 URL（可选，默认：OpenAI 官方 API）

#### 方法

- `List<String> sort(List<String> items)` - 字母顺序排序
- `List<String> sort(List<String> items, String criteria)` - 按指定条件排序

## 测试

运行单元测试：

```bash
# 设置环境变量
source .env

# 运行测试
mvn test
```

## 故障排除

### 常见问题

1. **API 密钥错误**
   ```
   错误: OpenAI API call failed: 401
   ```
   解决方案：检查 `OPENAI_API_KEY` 环境变量是否正确设置。

2. **网络连接问题**
   ```
   错误: java.net.ConnectException
   ```
   解决方案：检查网络连接和 `OPENAI_BASE_URL` 配置。

3. **JSON 解析错误**
   ```
   错误: InvalidParameter
   ```
   解决方案：确保使用的 API 端点兼容 OpenAI 格式。

### 调试模式

如需查看详细的 API 请求信息，可以临时添加调试输出：

```java
// 在 callOpenAI 方法中添加
System.out.println("请求 URL: " + baseUrl);
System.out.println("请求体: " + jsonBody);
```

## 环境要求

- Java 11 或更高版本
- 有效的 OpenAI API 密钥或兼容的 API 服务
- Maven 3.6+ （用于构建）

## 项目状态

- ✅ 核心功能完成
- ✅ 环境变量配置支持
- ✅ 单元测试通过
- ✅ 多 API 端点支持
- ✅ 错误处理完善

## 许可证

MIT License