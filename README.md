# 字段通用定义
- m_name 不为空，不为null，长度小于100，由汉字、大小写字母、数字、下划线（_）、连字符（-）构成
- m_simple_name 不为空，不为null，长度小于100，由汉字、大小写字母、数字组成
- m_price 不为空，不为null，两位小数，默认为0.00，范围：0.00-99999.99，
- m_text 不为空，不为null，最长5000个字符
- m_filename 不为空，可以null，长度小于100，由汉字、大小写字母、数字、下划线（_）、连字符（-）构成
- m_num 不为空，不为null，两位小数，默认为0.00，范围：0.00-99999.99，
- m_time 不为空，不为null，时间，格式：yyyy-MM-dd HH:mm:ss
  /* entity 实体类注解 */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @TableField(value = "create_time")
  private Date createTime;
- m_score 不为空，不为null，分数，int类型，范围0-200
- m_option 不为空，不为null，选项描述，由汉字、数字、大小写字母组成，长度小于20