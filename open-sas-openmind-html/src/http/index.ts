const protocol = "http";
const host = "127.0.0.1";
const port = "8080";
export const requestUrl = `${protocol}://${host}:${port}`;

export enum ResultEnum {
    OK = "00000",
    USER_REGISTER_USERNAME_ALREADY_EXISTS = "A0111",
    USER_LOGIN_ACCOUNT_NOT_EXIST = "A0201",
    USER_LOGIN_INCORRECT_PASSWORD = "A0210",
}