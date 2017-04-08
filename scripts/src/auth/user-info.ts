export class UserInfo {
  /**
   * @internal
   */
  protected constructor(protected _internalUser: InternalUser) {

  }

  get uid(): string {
    return this._internalUser.uid;
  }

  get displayName(): string | null {
    return this._internalUser.displayName;
  }

  get email(): string | null {
    return this._internalUser.email;
  }

  get photoURL(): string | null {
    return this._internalUser.photoURL;
  }

  get providerId(): string {
    return this._internalUser.providerId;
  }

}

/**
 * @internal
 */
export interface InternalUser {
  uid: string;
  displayName: string | null;
  email: string | null;
  photoURL: string | null;
  providerId: string;
  emailVerified: boolean;
  isAnonymous?: boolean;
  providerData?: InternalUser[];
}
