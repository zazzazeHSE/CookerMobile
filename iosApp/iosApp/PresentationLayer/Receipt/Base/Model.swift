import Foundation

enum Model<T> {
    case loading
    case data(T)
    case error(String)
}
