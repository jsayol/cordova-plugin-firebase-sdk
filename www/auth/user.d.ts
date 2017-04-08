import { UserInfo } from './user-info';
import { AuthCredential } from './index';
export declare class User extends UserInfo {
    private _auth;
    private _providerData;
    private _exec(success, error, action, args);
    readonly emailVerified: boolean;
    readonly isAnonymous: boolean;
    readonly providerData: UserInfo[];
    delete(): Promise<void>;
    getToken(forceRefresh?: boolean): Promise<string>;
    reload(): Promise<void>;
    sendEmailVerification(): Promise<void>;
    updateEmail(newEmail: string): Promise<void>;
    updatePassword(newPassword: string): Promise<void>;
    updateProfile(profile: {
        displayName?: string | null;
        photoURL?: string | null;
    }): Promise<void>;
    link(credential: AuthCredential): Promise<User>;
    unlink(providerId: string): Promise<User>;
    reauthenticate(credential: AuthCredential): Promise<void>;
    toJSON(): object;
    private _updateProviderData();
}
