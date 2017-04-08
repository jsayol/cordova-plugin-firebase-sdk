import { exec, SuccessCallback, ErrorCallback } from '../utils';
import { User } from './user';
import { InternalUser } from './user-info';
import { AuthCredential } from './providers';
import { App } from '../app';

export class Auth {
  private _currentUser: User = null;
  private _authListenerId = 0;
  private _authListeners: { [id: number]: AuthStateListener } = {};

  /**
   * @internal
   */
  constructor(public app: App) {

  }

  private _exec(success: SuccessCallback, error: ErrorCallback, action: string, args: any[]) {
    exec(success, error, 'Firebase', `auth_${action}`, [this.app.name, ...args]);
  }

  get currentUser(): User {
    return this._currentUser;
  }

  applyActionCode(code: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'applyActionCode', [code]);
    });
  }

  checkActionCode(code: string): Promise<ActionCodeInfo> {
    return new Promise<ActionCodeInfo>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success = (email: string) => {
        resolve({data: {email}});
      };
      this._exec(success, reject, 'checkActionCode', [code]);
    });
  }

  createUserWithEmailAndPassword(email: string, password: string): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success = (internalUserInfo: InternalUser) => {
        resolve(new User(this, internalUserInfo));
      };
      this._exec(success, reject, 'createUserWithEmailAndPassword', [email, password]);
    });
  }

  fetchProvidersForEmail(email: string): Promise<string[]> {
    return new Promise<string[]>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'fetchProvidersForEmail', [email]);
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
        resolve(new User(this, internalUserInfo));
      };
      this._exec(success, reject, 'signInAnonymously', []);
    });
  }

  signInWithCredential(credential: AuthCredential): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'signInWithCredential', [credential]);
    });
  }

  signInWithCustomToken(token: string): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success = (internalUserInfo: InternalUser) => {
        resolve(new User(this, internalUserInfo));
      };
      this._exec(success, reject, 'signInWithCustomToken', [token]);
    });
  }

  signInWithEmailAndPassword(email: string, password: string): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success = (internalUserInfo: InternalUser) => {
        resolve(new User(this, internalUserInfo));
      };
      this._exec(success, reject, 'signInWithEmailAndPassword', [email, password]);
    });
  }

  signOut(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'signOut', []);
    });
  }

  sendPasswordResetEmail(email: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'sendPasswordResetEmail', [email]);
    });
  }

  verifyPasswordResetCode(code: string): Promise<string> {
    return new Promise<string>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'verifyPasswordResetCode', [code]);
    });
  }

  confirmPasswordReset(code: string, newPassword: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'confirmPasswordReset', [code, newPassword]);
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
      this._currentUser = new User(this, internalUserInfo);

      for (let listenerId in this._authListeners) {
        this._authListeners[listenerId].callback(this._currentUser)
      }
    };

    const error = (reason?: any) => {
      for (let listenerId in this._authListeners) {
        this._authListeners[listenerId].errorFn(reason);
      }
    };

    this._exec(callback, error, 'addAuthStateListener', []);
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

export * from './providers';
export * from './user';
export * from './user-info';
