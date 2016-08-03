package com.tiagoderlan.showcase;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.tiagoderlan.showcase.database.DatabaseHelper;
import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.models.Content;
import com.tiagoderlan.showcase.models.Product;
import com.tiagoderlan.showcase.models.collections.CategoryCollection;
import com.tiagoderlan.showcase.models.collections.ProductCollection;

public class DatabaseTestCase extends AndroidTestCase {
    private DatabaseHelper db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = DatabaseHelper.getInstance(context);
    }

    @Override
    public void tearDown() throws Exception
    {
        db.close();
        super.tearDown();
    }


    public void testAddProductNull_ThrowNullPointerException()
    {
        try
        {
            db.addProduct(null);

            fail();
        }
        catch (NullPointerException e)
        {
            assertTrue(true);
        }
        catch (Exception e)
        {
            fail();
        }
    }

    public void testUpdateProductNull_ThrowNullPointerException()
    {
        try
        {
            db.updateProduct(null);

            fail();
        }
        catch (NullPointerException e)
        {
            assertTrue(true);
        }
        catch (Exception e)
        {
            fail();
        }
    }

    public void testAddCategoryNull_ThrowNullPointerException()
    {
        try
        {
            db.addCategory(null);

            fail();
        }
        catch (NullPointerException e)
        {
            assertTrue(true);
        }
        catch (Exception e)
        {
            fail();
        }
    }


    public void testAddContentNull_ThrowNullPointerException()
    {
        try
        {
            db.addContent(null);

            fail();
        }
        catch (NullPointerException e)
        {
            assertTrue(true);
        }
        catch (Exception e)
        {
            fail();
        }
    }

    public void testGetProductsByCategoryNull_ThrowNullPointerException()
    {
        try
        {
            ProductCollection products = db.getProductsByCategory(null);

            fail();
        }
        catch (NullPointerException e)
        {
            assertTrue(true);
        }
        catch (Exception e)
        {
            fail();
        }
    }

    public void testGetProductsEmpty_MustBeZeroLenght()
    {
        ProductCollection products = db.getProducts();

        assertEquals(0, products.size());
    }

    public void testGetCategoriesEmpty_MustBeZeroLenght()
    {
        CategoryCollection categories = db.getCategories();

        assertEquals(0, categories.size());
    }

    public void testGetCategoryNull_MustBeNull()
    {
        Category category = db.getCategory(null);

        assertNull(category);
    }

    public void testGetProductNull_MustBeNull()
    {
        Product product = db.getProduct(null);

        assertNull(product);
    }

    public void testGetContentEmpty_MustBeNull()
    {
        Content content = db.getContent("");

        assertNull(content);
    }
}



