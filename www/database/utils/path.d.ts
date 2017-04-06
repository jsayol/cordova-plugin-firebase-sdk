export declare class Path {
    private _parts;
    constructor(path?: string | string[]);
    readonly key: string | null;
    readonly parent: Path;
    readonly parts: string[];
    child(path: string, skipCheck?: boolean): Path;
    isEqual(otherPath: Path): boolean;
    toString(): string;
    readonly length: number;
    static getParts(path: string, skipCheck?: boolean): string[];
    static normalize(path: string, skipCheck?: boolean): string;
}
