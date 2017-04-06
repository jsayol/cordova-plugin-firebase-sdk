import { exec, SuccessCallback, ErrorCallback } from '../utils';
import { User } from './user';
import { InternalUser } from './user-info';
import { AuthCredential } from './providers';

export class Auth {
  private static _instance: Auth;

  private _currentUser: User = null;
  private _authListenerId = 0;
  private _authListeners: { [id: number]: AuthStateListener } = {};

  constructor() {
    // Prevent creating multiple instances
    if (Auth._instance) {
      return Auth._instance;
    }
  }

  static getInstance(): Auth {
    if (!this._instance) {
      this._instance = new Auth();
    }

    return this._instance;
  }

  get currentUser(): User {
    return this._currentUser;
  }

  applyActionCode(code: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'auth_applyActionCode', [code]);
    });
  }

  checkActionCode(code: string): Promise<ActionCodeInfo> {
    return new Promise<ActionCodeInfo>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success = (email: string) => {
        resolve({data: {email}});
      };
      exec(success, reject, 'Firebase', 'auth_checkActionCode', [code]);
    });
  }

  createUserWithEmailAndPassword(email: string, password: string): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success = (internalUserInfo: InternalUser) => {
        resolve(new User(internalUserInfo));
      };
      exec(success, reject, 'Firebase', 'auth_createUserWithEmailAndPassword', [email, password]);
    });
  }

  fetchProvidersForEmail(email: string): Promise<string[]> {
    return new Promise<string[]>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'auth_fetchProvidersForEmail', [email]);
    });
  }

  onAuthStateChanged(callback: (user: User) => any, errorFn?: ErrorCallback, completeFn?: () => any): () => void {
    const listenerId = this._authListenerId++;
    this._authListeners[listenerId] = {callback, errorFn, completeFn};

    if (Object.keys(this._authListeners).length === 1) {
      this._attachAuthStateChangeListener();
    }

    return () => this._removeAuthStateListener(listenerId);
  }

  signInAnonymously(): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success = (internalUserInfo: InternalUser) => {
        resolve(new User(internalUserInfo));
      };
      exec(success, reject, 'Firebase', 'auth_signInAnonymously', []);
    });
  }

  signInWithCredential(credential: AuthCredential): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'auth_signInWithCredential', [credential]);
    });
  }

  signInWithCustomToken(token: string): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success = (internalUserInfo: InternalUser) => {
        resolve(new User(internalUserInfo));
      };
      exec(success, reject, 'Firebase', 'auth_signInWithCustomToken', [token]);
    });
  }

  signInWithEmailAndPassword(email: string, password: string): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success = (internalUserInfo: InternalUser) => {
        resolve(new User(internalUserInfo));
      };
      exec(success, reject, 'Firebase', 'auth_signInWithEmailAndPassword', [email, password]);
    });
  }

  signOut(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'auth_signOut', []);
    });
  }

  sendPasswordResetEmail(email: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'auth_sendPasswordResetEmail', [email]);
    });
  }

  verifyPasswordResetCode(code: string): Promise<string> {
    return new Promise<string>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'auth_verifyPasswordResetCode', [code]);
    });
  }

  confirmPasswordReset(code: string, newPassword: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'auth_confirmPasswordReset', [code, newPassword]);
    });
  }

  private _removeAuthStateListener(listenerId: number): void {
    if (this._authListeners[listenerId]) {
      const listener = this._authListeners[listenerId];

      if (typeof listener.completeFn === 'function') {
        listener.completeFn();
      }

      delete this._authListeners[listenerId];

      if (Object.keys(this._authListeners).length === 0) {
        this._detachAuthStateChangeListener();
      }
    }
  }

  private _attachAuthStateChangeListener() {
    const callback = (internalUserInfo: InternalUser) => {
      this._currentUser = new User(internalUserInfo);

      for (let listenerId in this._authListeners) {
        this._authListeners[listenerId].callback(this._currentUser)
      }
    };

    const error = (reason?: any) => {
      for (let listenerId in this._authListeners) {
        this._authListeners[listenerId].errorFn(reason);
      }
    };

    exec(callback, error, 'Firebase', 'auth_addAuthStateListener', []);
  }

  private _detachAuthStateChangeListener() {
    const error = (reason: any) => {
      throw new Error('Failed detaching auth state change listener:' + reason);
    };
    exec(() => {
    }, error, 'Firebase', 'auth_removeAuthStateListener', []);
  }

}

export interface ActionCodeInfo {
  data: {
    email: string;
  }
}

export interface AuthStateListener {
  callback: (user: User) => any;
  errorFn?: ErrorCallback;
  completeFn?: () => any;
}

export const auth = () => Auth.getInstance();

export * from './providers';
export * from './user';
export * from './user-info';
