import axios from "axios";

axios.defaults.withCredentials = true;

//登录接口
export async function login(email, password) {
  const loginForm = new FormData();
  loginForm.append("email", email);
  loginForm.append("password", password);

  const loginPost = {
    method: "POST",
    url: "/api/login",
    headers: { "content-type": "multipart/form-data" },
    data: loginForm,
  };

  try {
    const response = await axios.request(loginPost);
    return response.data.info == "请求成功" ? "登陆成功" : response.data.info; //取出表单中的info字段做判断
  } catch (error) {
    console.error(error);
    return response.data;
  }
}

//注册接口
export async function register(email, username, password) {
  const registerForm = new FormData();
  registerForm.append("email", email);
  registerForm.append("nickName", username);
  registerForm.append("password", password);

  const registerPost = {
    method: "POST",
    url: "http://frp-fly.top:31031/api/register",
    headers: { "content-type": "multipart/form-data" },
    data: registerForm,
  };

  try {
    const response = await axios.request(registerPost);
    return response.data.info == "请求成功" ? "注册成功" : response.data.info; //取出表单中的info字段做判断
  } catch (error) {
    console.error(error);
    return response.data;
  }
}

//发帖接口
export async function forumPost(title, content) {
  const forumPostForm = new FormData();
  forumPostForm.append("cover", "");
  forumPostForm.append("pBoardId", "10000");
  forumPostForm.append("boardId", "");
  forumPostForm.append("title", title);
  forumPostForm.append("content", content);
  forumPostForm.append("markdownContent", "");
  forumPostForm.append("editorType", "0");
  forumPostForm.append("summary", "");

  const forumPost = {
    method: "POST",
    url: "/api/forum/postArticle",
    headers: { "content-type": "multipart/form-data" },
    data: forumPostForm,
  };

  try {
    const response = await axios.request(forumPost);
    return response.data.info == "请求成功" ? "发帖成功" : "发帖失败";
  } catch (error) {
    console.error(error);
    return response.data;
  }
}

//获取帖子列表
export async function articleList(articleListData) {
  const articleListPost = {
    method: "POST",
    url: "/api/forum/loadArticle",
    headers: { "content-type": "multipart/form-data" },
  };

  try {
    const response = await axios.request(articleListPost);
    return response.data.data.list;
  } catch (error) {
    console.error(error);
    return response.data.info;
  }
}

//查看帖子详情
export async function articleDetail(articleId) {
  const articleDetailForm = new FormData();
  articleDetailForm.append("articleId", articleId);

  const articleDetailPost = {
    method: "POST",
    url: "/api/forum/getArticleDetail",
    headers: { "content-type": "multipart/form-data" },
    data: articleDetailForm,
  };

  try {
    const response = await axios.request(forumPost);
    return response.data.data.forumArticle;
  } catch (error) {
    console.error(error);
    return response.data;
  }
}
