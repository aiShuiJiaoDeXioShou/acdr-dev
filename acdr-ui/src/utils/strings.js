export function truncateString(str) {
  // 设置最大字符长度
  const maxLength = 400

  // 检查字符串长度是否超过最大长度
  if (str.length > maxLength) {
    // 如果超过，截断字符串并添加省略号
    return str.substring(0, maxLength) + '...'
  }

  // 如果没有超过，直接返回原字符串
  return str
}
