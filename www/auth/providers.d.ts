export interface AuthCredential {
    provider: string;
}
export interface AuthProvider {
    providerId: string;
}
export declare class EmailAuthProvider implements AuthProvider {
    static PROVIDER_ID: string;
    static credential(email: string, password: string): AuthCredential;
    readonly providerId: string;
}
export declare class FacebookAuthProvider implements AuthProvider {
    static PROVIDER_ID: string;
    static credential(token: string): AuthCredential;
    readonly providerId: string;
}
export declare class GithubAuthProvider implements AuthProvider {
    static PROVIDER_ID: string;
    static credential(token: string): AuthCredential;
    readonly providerId: string;
}
export declare class GoogleAuthProvider implements AuthProvider {
    static PROVIDER_ID: string;
    static credential(idToken?: string | null, accessToken?: string | null): AuthCredential;
    readonly providerId: string;
}
export declare class TwitterAuthProvider implements AuthProvider {
    static PROVIDER_ID: string;
    static credential(token: string, secret: string): AuthCredential;
    readonly providerId: string;
}
