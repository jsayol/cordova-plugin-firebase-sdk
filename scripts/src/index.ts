import { exec, ErrorCallback, SuccessCallback } from './utils';

export function initializeApp(config: { [k: string]: string }): Promise<void> {
  return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
    exec(resolve, reject, 'Firebase', 'initializeApp', [config]);
  });
}

export {
  auth,
  Auth,
  User,
  UserInfo,
  ActionCodeInfo,
  AuthStateListener,
  AuthCredential,
  EmailAuthProvider,
  FacebookAuthProvider,
  GithubAuthProvider,
  GoogleAuthProvider,
  TwitterAuthProvider,
} from './auth';

export {
  database,
  Database,
  DataSnapshot,
  EventListenerCallback,
  Query,
  Reference,
  ThenableReference,
  OnDisconnect,
} from './database';

export { analytics, Analytics, LogEventParams } from './analytics';

export { messaging, Messaging } from './messaging';

export { remoteConfig, RemoteConfig, ConfigInfo, ConfigSettings, ConfigDefaults } from './remote-config';

export { ErrorCallback, SuccessCallback } from './utils';