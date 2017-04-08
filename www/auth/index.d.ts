import { ErrorCallback } from '../utils';
import { User } from './user';
import { AuthCredential } from './providers';
import { App } from '../app';
export declare class Auth {
    app: App;
    private _currentUser;
    private _authListenerId;
    private _authListeners;
    private _exec(success, error, action, args);
    readonly currentUser: User;
    applyActionCode(code: string): Promise<void>;
    checkActionCode(code: string): Promise<ActionCodeInfo>;
    createUserWithEmailAndPassword(email: string, password: string): Promise<User>;
    fetchProvidersForEmail(email: string): Promise<string[]>;
    onAuthStateChanged(callback: (user: User) => any, errorFn?: ErrorCallback, completeFn?: () => any): () => void;
    signInAnonymously(): Promise<User>;
    signInWithCredential(credential: AuthCredential): Promise<User>;
    signInWithCustomToken(token: string): Promise<User>;
    signInWithEmailAndPassword(email: string, password: string): Promise<User>;
    signOut(): Promise<void>;
    sendPasswordResetEmail(email: string): Promise<void>;
    verifyPasswordResetCode(code: string): Promise<string>;
    confirmPasswordReset(code: string, newPassword: string): Promise<void>;
    private _removeAuthStateListener(listenerId);
    private _attachAuthStateChangeListener();
    private _detachAuthStateChangeListener();
}
export interface ActionCodeInfo {
    data: {
        email: string;
    };
}
export interface AuthStateListener {
    callback: (user: User) => any;
    errorFn?: ErrorCallback;
    completeFn?: () => any;
}
export * from './providers';
export * from './user';
export * from './user-info';
