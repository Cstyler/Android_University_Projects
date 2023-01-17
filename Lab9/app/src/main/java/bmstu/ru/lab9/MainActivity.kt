package bmstu.ru.lab9

import android.Manifest
import android.app.AlertDialog
import android.app.ListActivity
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import java.io.File
import java.io.IOException


class MainActivity : ListActivity() {
    companion object {
        private const val REQUEST_PERMISSION = 11
        private const val PARENT_DIRECTORY_NAME = ".."
        private const val CREATE_FILENAME = "file.txt"
        private const val TAG = "MainActivity"
    }

    private val rootPath = Environment.getExternalStorageDirectory().path
    private var curDir: String = rootPath
    private var parentDir: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        val fileNames = listFilesInCurDir()
        listAdapter = createArrayAdapter(fileNames)
        Log.i(TAG, "OnCreate")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
            if (parentDir != null) {
                moveToParent()
            }
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun moveToParent() {
        val file = File(parentDir)
        curDir = parentDir!!
        parentDir = if (curDir == rootPath) {
            null
        } else {
            file.parent
        }
        setAdapter(file)
    }

    private fun setAdapter(file: File) {
        val arrayFiles = file.listFiles()
        val firstElem = if (parentDir == null)
            arrayOf() else arrayOf(PARENT_DIRECTORY_NAME)
        val arrayFileNames = firstElem + fileList2fileNames(arrayFiles)
        listAdapter = createArrayAdapter(arrayFileNames)
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        if (parentDir != null && position == 0) {
            moveToParent()
        } else {
            val item = listAdapter.getItem(position) as String
            val file = File(curDir, item)
            if (file.isDirectory) {
                parentDir = curDir
                curDir = file.absolutePath
                setAdapter(file)
                return
            } else {
                showDeleteDialog(file)
            }
        }
    }

    private fun showDeleteDialog(file: File) {
        val dialog = AlertDialog.Builder(this).create()
        dialog.setTitle(getString(R.string.dialog_delete_file_title))
        dialog.setCancelable(false)
        dialog.setButton(
            DialogInterface.BUTTON_POSITIVE,
            getString(R.string.dialog_delete_file_yes)
        )
        { _, _ ->
            file.delete()
            refreshDirList()
        }
        dialog.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            getString(R.string.dialog_delete_file_no)
        )
        { _, _ -> }
        dialog.setIcon(android.R.drawable.ic_dialog_alert)
        dialog.show()
    }

    private fun fileList2fileNames(arrayFiles: Array<File>): Array<String> {
        return arrayFiles.map {
            it.name
        }.toTypedArray()
    }

    private fun listFilesInCurDir(): Array<String> {
        val file = File(curDir)
        val arrayFiles = file.listFiles()
        return fileList2fileNames(arrayFiles)
    }

    private fun createArrayAdapter(stringArray: Array<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            this,
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            stringArray
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.createFileButton -> {
                createFile()
                refreshDirList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshDirList() {
        val file = File(curDir)
        setAdapter(file)
    }

    private fun createFile() {
        val file = File(curDir, CREATE_FILENAME)
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun requestPermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        requestPermissions(permissions, REQUEST_PERMISSION)
    }
}
