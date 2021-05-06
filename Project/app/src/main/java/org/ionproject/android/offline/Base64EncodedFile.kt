package org.ionproject.android.offline

/**
 * The github link to info from the tree gives us the file content
 * encoded in base64
 *
 * The solution is keep a separate URL builder where we can just change the
 * name of the branch and the name of the file so we can decode directly to the
 * data classes, or receive the files encoded and then decode to their respective data classes
 */
data class Base64EncodedFile(
    var content: String
)