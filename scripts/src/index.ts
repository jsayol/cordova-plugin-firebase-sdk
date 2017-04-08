import { App, AppOptions } from './app';
import { Auth } from './auth';
import { Database } from './database';
import { Analytics } from './analytics';
import { Messaging } from './messaging';
import { RemoteConfig } from './remote-config';

const apps: { [k: string]: App } = {};
let defaultApp: App;

export function app(name = App.DEFAULT_NAME) {
  if (!apps.hasOwnProperty(name)) {
    throw new Error(`There is no app named ${name}`);
  }
  return apps[name];
}

export function initializeApp(options?: AppOptions, name = App.DEFAULT_NAME): App {
  if ((name === void 0) && defaultApp) {
    throw new Error('The default app has already been initialized');
  }

  const app = new App(options, name);

  apps[name] = app;

  if (name === App.DEFAULT_NAME) {
    defaultApp = app;
  }

  return app;
}

export function auth(): Auth {
  if (!defaultApp) {
    initializeApp();
  }

  return defaultApp.auth();
}

export function database(): Database {
  if (!defaultApp) {
    initializeApp();
  }

  return defaultApp.database();
}

export function analytics(): Analytics {
  if (!defaultApp) {
    initializeApp();
  }

  return defaultApp.analytics();
}

export function messaging(): Messaging {
  if (!defaultApp) {
    initializeApp();
  }

  return defaultApp.messaging();
}

export function remoteConfig(): RemoteConfig {
  if (!defaultApp) {
    initializeApp();
  }

  return defaultApp.remoteConfig();
}

export {
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
  Database,
  DataSnapshot,
  EventListenerCallback,
  Query,
  Reference,
  ThenableReference,
  OnDisconnect,
} from './database';

export { Analytics, LogEventParams } from './analytics';

export { Messaging } from './messaging';

export { RemoteConfig, ConfigInfo, ConfigSettings, ConfigDefaults } from './remote-config';

export { ErrorCallback, SuccessCallback } from './utils';