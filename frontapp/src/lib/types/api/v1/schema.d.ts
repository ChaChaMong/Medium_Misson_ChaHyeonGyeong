/**
 * This file was auto-generated by openapi-typescript.
 * Do not make direct changes to the file.
 */


export interface paths {
  "/api/v1/posts/{id}": {
    /** 글 상세 조회 */
    get: operations["getPost"];
    /** 글 수정 */
    put: operations["modifyPost"];
    /** 글 삭제 */
    delete: operations["deletePost"];
  };
  "/api/v1/posts": {
    /** 글 작성 */
    post: operations["writePost"];
  };
  "/api/v1/members/logout": {
    /** 로그아웃 */
    post: operations["logout"];
  };
  "/api/v1/members/login": {
    /** 로그인, 로그인 성공시 accessToken, refreshToken 쿠키 설정 */
    post: operations["login"];
  };
  "/api/v1/members/join": {
    /** 회원가입 */
    post: operations["join"];
  };
  "/api/v1/posts/{id}/modify": {
    /** 수정 화면 글 조회 */
    get: operations["showModify"];
  };
  "/api/v1/posts/myList": {
    /** 내 글 리스트 */
    get: operations["getMyPosts"];
  };
  "/api/v1/posts/list": {
    /** 글 리스트 */
    get: operations["getPosts"];
  };
  "/api/v1/posts/latest": {
    /** 최신 글 리스트 */
    get: operations["getLatestPosts"];
  };
  "/api/v1/members/me": {
    /** 내 정보 */
    get: operations["getMe"];
  };
  "/api/v1/b/{username}": {
    /** 특정 사용자의 글 리스트 */
    get: operations["getPostsByUsername"];
  };
  "/api/v1/b/{username}/{id}": {
    /** 특정 사용자의 글 상세 조회 */
    get: operations["getPostById"];
  };
}

export type webhooks = Record<string, never>;

export interface components {
  schemas: {
    Empty: Record<string, never>;
    RsDataEmpty: {
      resultCode: string;
      msg: string;
      data: components["schemas"]["Empty"];
      /** Format: int32 */
      statusCode: number;
      success: boolean;
      fail: boolean;
    };
    PostRequestDto: {
      title: string;
      body: string;
      published?: boolean;
    };
    PostDto: {
      /** Format: int64 */
      id: number;
      createDate: string;
      modifyDate: string;
      /** Format: int64 */
      authorId: number;
      authorName: string;
      title: string;
      body: string;
      paid: boolean;
      published: boolean;
    };
    RsDataPostDto: {
      resultCode: string;
      msg: string;
      data: components["schemas"]["PostDto"];
      /** Format: int32 */
      statusCode: number;
      success: boolean;
      fail: boolean;
    };
    LoginRequestDto: {
      username: string;
      password: string;
    };
    LoginResponseDto: {
      item: components["schemas"]["MemberDto"];
      refreshToken: string;
      accessToken: string;
    };
    MemberDto: {
      /** Format: int64 */
      id: number;
      /** Format: date-time */
      createDate: string;
      /** Format: date-time */
      modifyDate: string;
      username: string;
      authorities: string[];
      paid: boolean;
    };
    RsDataLoginResponseDto: {
      resultCode: string;
      msg: string;
      data: components["schemas"]["LoginResponseDto"];
      /** Format: int32 */
      statusCode: number;
      success: boolean;
      fail: boolean;
    };
    JoinRequestDto: {
      username: string;
      password: string;
      passwordConfirm: string;
    };
    RsDataMemberDto: {
      resultCode: string;
      msg: string;
      data: components["schemas"]["MemberDto"];
      /** Format: int32 */
      statusCode: number;
      success: boolean;
      fail: boolean;
    };
    PageDtoPostDto: {
      /** Format: int64 */
      totalElementsCount: number;
      /** Format: int64 */
      pageElementsCount: number;
      /** Format: int64 */
      totalPagesCount: number;
      /** Format: int32 */
      number: number;
      content: components["schemas"]["PostDto"][];
      hasPrevious: boolean;
      hasNext: boolean;
    };
    RsDataPageDtoPostDto: {
      resultCode: string;
      msg: string;
      data: components["schemas"]["PageDtoPostDto"];
      /** Format: int32 */
      statusCode: number;
      success: boolean;
      fail: boolean;
    };
    RsDataListPostDto: {
      resultCode: string;
      msg: string;
      data: components["schemas"]["PostDto"][];
      /** Format: int32 */
      statusCode: number;
      success: boolean;
      fail: boolean;
    };
  };
  responses: never;
  parameters: never;
  requestBodies: never;
  headers: never;
  pathItems: never;
}

export type $defs = Record<string, never>;

export type external = Record<string, never>;

export interface operations {

  /** 글 상세 조회 */
  getPost: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 글 수정 */
  modifyPost: {
    parameters: {
      path: {
        id: number;
      };
    };
    requestBody: {
      content: {
        "application/json": components["schemas"]["PostRequestDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 글 삭제 */
  deletePost: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 글 작성 */
  writePost: {
    requestBody: {
      content: {
        "application/json": components["schemas"]["PostRequestDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 로그아웃 */
  logout: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataEmpty"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 로그인, 로그인 성공시 accessToken, refreshToken 쿠키 설정 */
  login: {
    requestBody: {
      content: {
        "application/json": components["schemas"]["LoginRequestDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataLoginResponseDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 회원가입 */
  join: {
    requestBody: {
      content: {
        "application/json": components["schemas"]["JoinRequestDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataMemberDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 수정 화면 글 조회 */
  showModify: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 내 글 리스트 */
  getMyPosts: {
    parameters: {
      query?: {
        page?: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPageDtoPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 글 리스트 */
  getPosts: {
    parameters: {
      query?: {
        page?: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPageDtoPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 최신 글 리스트 */
  getLatestPosts: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataListPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 내 정보 */
  getMe: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataMemberDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 특정 사용자의 글 리스트 */
  getPostsByUsername: {
    parameters: {
      query?: {
        page?: number;
      };
      path: {
        username: string;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPageDtoPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 특정 사용자의 글 상세 조회 */
  getPostById: {
    parameters: {
      path: {
        username: string;
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPostDto"];
        };
      };
      /** @description Internal Server Error */
      500: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
}
