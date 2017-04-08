import { ErrorCallback, getValueType, isInteger, SuccessCallback } from '../utils';
import { Database, EventListenerCallback } from './index';
import { Path } from './utils/path';
import { Reference } from './reference';
import { DataSnapshot } from './data-snapshot';

export class Query {
  protected _path: Path;
  protected _db: Database;
  protected _options: { [k: string]: any };

  constructor(query: Query, options: { [k: string]: any });
  constructor(path: string | Path, db: Database);
  constructor(pathOrQuery: string | Path | Query, _dbOrOptions?: Database | { [k: string]: any }) {
    if (pathOrQuery instanceof Query) {
      this._path = pathOrQuery._path;
      this._db = pathOrQuery._db;
      this._options = _dbOrOptions;
      return;
    }

    this._path = (pathOrQuery instanceof Path) ? pathOrQuery : new Path(pathOrQuery);
    this._db = <Database>_dbOrOptions;
    this._options = {
      _hasOrder: false,
      startAt: {
        value: void 0,
        valueType: void 0,
        key: void 0,
      },
      endAt: {
        value: void 0,
        valueType: void 0,
        key: void 0,
      },
      equalTo: {
        value: void 0,
        valueType: void 0,
        key: void 0,
      },
      limitToFirst: void 0,
      limitToLast: void 0,
      orderByKey: void 0,
      orderByChild: void 0,
      orderByPriority: false,
      orderByValue: false,
    };

  }

  protected _exec(success: SuccessCallback, error: ErrorCallback, action: string, args: any[]) {
    this._db._exec(success, error, action, args);
  }

  get ref(): Reference {
    return new Reference(this._path, this._db);
  }

  startAt(value: number | string | boolean | null, key?: string): Query {
    if (this._options.startAt !== void 0) {
      throw new Error('Query.startAt: either startAt or equalTo has already been set');
    }

    const valueType = getValueType(value);

    if ((valueType !== 'number') && (valueType !== 'string') && (valueType !== 'boolean') && (valueType !== 'null')) {
      throw new Error(`Query.startAt: value must be either a number, a string, or a boolean. "${valueType}" is not a valid type.`);
    }

    if (key !== void 0) {
      if (typeof key !== 'string') {
        throw new Error(`Query.startAt: key must be either a string`);
      }
    }

    return new Query(this, {...this._options, startAt: {value, valueType, key}});
  }

  endAt(value: number | string | boolean | null, key?: string): Query {
    if (this._options.endAt !== void 0) {
      throw new Error('Query.endAt: either endAt or equalTo has already been set');
    }

    const valueType = getValueType(value);

    if ((valueType !== 'number') && (valueType !== 'string') && (valueType !== 'boolean') && (valueType !== 'null')) {
      throw new Error(`Query.endAt: value must be either a number, a string, or a boolean. "${valueType}" is not a valid type.`);
    }

    if (key !== void 0) {
      if (typeof key !== 'string') {
        throw new Error(`Query.endAt: key must be either a string`);
      }
    }

    return new Query(this, {...this._options, endAt: {value, valueType, key}});
  }

  equalTo(value: number | string | boolean | null, key?: string): Query {
    if ((this._options.startAt !== void 0) || (this._options.endAt !== void 0)) {
      throw new Error('Query.equalTo: either startAt, endAt, or equalTo has already been set');
    }

    const valueType = getValueType(value);

    if ((valueType !== 'number') && (valueType !== 'string') && (valueType !== 'boolean') && (valueType !== 'null')) {
      throw new Error(`Query.equalTo: value must be either a number, a string, or a boolean. "${valueType}" is not a valid type.`);
    }

    if (key !== void 0) {
      if (typeof key !== 'string') {
        throw new Error(`Query.equalTo: key must be either a string`);
      }
    }

    return new Query(this, {...this._options, equalTo: {value, valueType, key}});
  }

  limitToFirst(limit: number): Query {
    if (!isInteger(limit)) {
      throw new Error('Query.limitToFirst: value is not an integer');
    }
    return new Query(this, {...this._options, limitToFirst: [limit]});
  }

  limitToLast(limit: number): Query {
    if (!isInteger(limit)) {
      throw new Error('Query.limitToLast: value is not an integer');
    }
    return new Query(this, {...this._options, limitToLast: [limit]});
  }

  orderByKey(): Query {
    if (this._options.hasOrder) {
      throw new Error('Query.orderByKey: order has already been set');
    }
    return new Query(this, {...this._options, hasOrder: true, orderByKey: true});
  }

  orderByChild(path: string): Query {
    if (this._options.hasOrder) {
      throw new Error('Query.orderByChild: order has already been set');
    }
    if (typeof path !== 'string') {
      throw new Error('Query.orderByChild: path needs to be a string');
    }
    return new Query(this, {...this._options, hasOrder: true, orderByChild: [path]});
  }

  orderByPriority(): Query {
    if (this._options.hasOrder) {
      throw new Error('Query.orderByPriority: order has already been set');
    }
    return new Query(this, {...this._options, hasOrder: true, orderByPriority: true});
  }

  orderByValue(): Query {
    if (this._options.hasOrder) {
      throw new Error('Query.orderByValue: order has already been set');
    }
    return new Query(this, {...this._options, hasOrder: true, orderByValue: true});
  }

  isEqual(query: Query): boolean {
    return (this._db === query._db)
      && (this._path.isEqual(query._path))
      && (this._options.startAt.value === query._options.startAt.value)
      && (this._options.startAt.valueType === query._options.startAt.valueType)
      && (this._options.startAt.key === query._options.startAt.key)
      && (this._options.endAt.value === query._options.endAt.value)
      && (this._options.endAt.valueType === query._options.endAt.valueType)
      && (this._options.endAt.key === query._options.endAt.key)
      && (this._options.equalTo.value === query._options.equalTo.value)
      && (this._options.equalTo.valueType === query._options.equalTo.valueType)
      && (this._options.equalTo.key === query._options.equalTo.key)
      && (this._options.limitToFirst === query._options.limitToFirst)
      && (this._options.limitToLast === query._options.limitToLast)
      && (this._options.hasOrder === query._options.hasOrder)
      && (this._options.orderByKey === query._options.orderByKey)
      && (this._options.orderByChild === query._options.orderByChild)
      && (this._options.orderByPriority === query._options.orderByPriority)
      && (this._options.orderByValue === query._options.orderByValue);
  }

  on(eventType: string, callback: EventListenerCallback, errorFn?: (err?: Error) => any): EventListenerCallback {
    this._attachListener(false, eventType, callback, errorFn);
    return callback;
  }

  once(eventType: string, callback?: EventListenerCallback, errorFn?: (err?: Error) => any): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const success: SuccessCallback = (value?: any) => {
        if (typeof callback === 'function') {
          callback(value);
        }
        resolve(value);
      };

      const error: ErrorCallback = (reason?: any) => {
        if (typeof errorFn === 'function') {
          errorFn(reason);
        }
        reject(reason);
      };

      this._attachListener(true, eventType, success, error);
    });
  }

  private _attachListener(once: boolean = false, eventType: string = 'value', callback: EventListenerCallback, errorFn?: (err?: Error) => any) {
    const listenerID = String(this._db._listenerCounter++);
    this._addListener(listenerID, this._path, eventType, callback);

    const success: SuccessCallback = (listenerEvent: ListenerEvent) => {
      if (once) {
        this._removeListener(listenerID);
      }
      const snapshot = new DataSnapshot(this._db, this._path, listenerEvent.value, listenerEvent.key);
      callback(snapshot, listenerEvent.prevChildKey);
    };

    const error: ErrorCallback = (reason: any) => {
      this._removeListener(listenerID);
      if (typeof errorFn === 'function') {
        errorFn(new Error(reason));
      }
    };

    const queryOptions: { [k: string]: any } = {};

    if (this._options.hasOrder) {
      if (this._options.orderByValue) {
        queryOptions.orderByValue = true;
      } else if (this._options.orderByPriority) {
        queryOptions.orderByPriority = true;
      } else if (this._options.orderByChild !== void 0) {
        queryOptions.orderByChild = this._options.orderByChild;
      } else if (this._options.orderByKey) {
        queryOptions.orderByKey = true;
      }
    }

    if (this._options.limitToFirst !== void 0) {
      queryOptions.limitToFirst = this._options.limitToFirst;
    } else if (this._options.limitToLast !== void 0) {
      queryOptions.limitToLast = this._options.limitToLast;
    }

    if (this._options.equalTo.value !== void 0) {
      queryOptions.equalTo = this._options.equalTo;
    } else {
      if (this._options.startAt.value !== void 0) {
        queryOptions.startAt = this._options.startAt;
      }
      if (this._options.endAt.value !== void 0) {
        queryOptions.endAt = this._options.endAt;
      }
    }

    this._exec(success, error, 'attachListener', [listenerID, once, this._path.toString(), eventType, queryOptions]);
  }

  off(eventType?: string, callback?: EventListenerCallback): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      let listenerIDs: string[];

      if (eventType && callback) {
        listenerIDs = [this._getListenerID(eventType, callback)];
      } else {
        listenerIDs = this._getListenerIDs(eventType, callback);
      }

      if (!listenerIDs || (listenerIDs.length === 0)) {
        resolve();
        return;
      }

      const success: SuccessCallback = () => {
        listenerIDs.forEach(id => this._removeListener(id));
        resolve();
      };

      this._exec(success, reject, 'removeListeners', [listenerIDs]);
    });
  }

  keepSynced(keepSynced: boolean): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(() => resolve(), reject, 'keepSynced', [this._path.toString(), keepSynced]);
    });
  }

  private _removeListener(id: string | null) {
    if (id) {
      delete this._db._listeners[id];
    }
  }

  private _addListener(id: string, path: Path, eventType: string, callback: EventListenerCallback) {
    this._db._listeners[id] = {path, eventType, callback};
  }

  private _getListenerID(eventType: string, callback: EventListenerCallback): string {
    const keys = Object.keys(this._db._listeners);

    for (let i = 0, l = keys.length; i < l; i++) {
      const id = keys[i];
      const listener = this._db._listeners[id];

      if ((listener.path === this._path) && (listener.eventType === eventType) && (listener.callback === callback)) {
        return id;
      }
    }

    // Not found
    return null;
  }

  private _getListenerIDs(eventType?: string, callback?: EventListenerCallback): string[] {
    const keys = Object.keys(this._db._listeners);
    const ids: string[] = [];

    for (let i = 0, l = keys.length; i < l; i++) {
      const id = keys[i];
      const listener = this._db._listeners[id];

      if (listener.path.isEqual(this._path)
        && (!eventType || (listener.eventType === eventType))
        && (!callback || (listener.callback === callback))) {
        ids.push(id);
      }
    }

    return ids;
  }

}

export interface ListenerEvent {
  key?: string;
  value: any;
  prevChildKey?: string
}
