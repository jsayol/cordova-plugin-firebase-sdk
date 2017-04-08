import { exec, SuccessCallback, ErrorCallback } from '../utils';
import { InternalUser, UserInfo } from './user-info';
import { Auth, AuthCredential } from './index';

export class User extends UserInfo {
  private _providerData: UserInfo[];

  /**
   * @internal
   */
  constructor(private _auth: Auth, internalUser: InternalUser) {
    super(internalUser);
    this._updateProviderData();
  }

  private _exec(success: SuccessCallback, error: ErrorCallback, action: string, args: any[]) {
    exec(success, error, 'Firebase', `auth_user_${action}`, [this._auth.app.name, ...args]);
  }

  get emailVerified(): boolean {
    return this._internalUser.emailVerified;

  }

  get isAnonymous(): boolean {
    return this._internalUser.isAnonymous;
  }

  get providerData(): UserInfo[] {
    // Return a copy of the providerData array to prevent accidental
    // modifications to the internal representation.
    return this._providerData.slice();
  }

  delete(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'delete', [this.uid]);
    });
  }

  getToken(forceRefresh = false): Promise<string> {
    return new Promise<string>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'getToken', [this.uid, forceRefresh]);
    });
  }

  reload(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const callback = (internalUser: InternalUser) => {
        this._internalUser = internalUser;
        this._updateProviderData();
        resolve();
      };
      this._exec(callback, reject, 'reload', [this.uid]);
    });
  }

  sendEmailVerification(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'sendEmailVerification', [this.uid]);
    });
  }

  updateEmail(newEmail: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'updateEmail', [this.uid, newEmail]);
    });
  }

  updatePassword(newPassword: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'updatePassword', [this.uid, newPassword]);
    });
  }

  updateProfile(profile: { displayName?: string | null, photoURL?: string | null }): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const _profile: { displayName?: string | null, photoURL?: string | null } = {};
      let hasDisplayName = false;
      let hasPhotoURL = false;

      if (profile.displayName !== void 0) {
        hasDisplayName = true;
        _profile.displayName = profile.displayName;
      }

      if (profile.photoURL !== void 0) {
        hasPhotoURL = true;
        _profile.photoURL = profile.photoURL;
      }

      if (!hasDisplayName && !hasPhotoURL) {
        reject('You need to specify at least one of displayName or photoURL');
        return;
      }

      const callback = () => {
        if (hasDisplayName) {
          this._internalUser.displayName = profile.displayName;
        }

        if (hasPhotoURL) {
          this._internalUser.photoURL = profile.photoURL;
        }

        resolve();
      };

      this._exec(callback, reject, 'updateProfile', [this.uid, _profile]);
    });
  }

  link(credential: AuthCredential): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'link', [this.uid, credential]);
    });
  }

  unlink(providerId: string): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const callback = (internalUser: InternalUser) => {
        this._internalUser = internalUser;
        this._updateProviderData();
        resolve();
      };
      this._exec(callback, reject, 'unlink', [this.uid, providerId]);
    });
  }

  reauthenticate(credential: AuthCredential): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'reauthenticate', [this.uid, credential]);
    });
  }

  toJSON(): object {
    return JSON.parse(JSON.stringify(this._internalUser));
  }

  private _updateProviderData() {
    this._providerData = [];
    this._internalUser.providerData.forEach((providerUser: InternalUser) => {
      this._providerData.push(new UserInfo(providerUser));
    });
  }

}
